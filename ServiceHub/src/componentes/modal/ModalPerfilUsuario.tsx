import { useEffect, useState, type ChangeEvent, type FormEvent } from "react";
import { Pencil, Save, UserRound, X } from "lucide-react";
import type { Especialidade } from "../../servicos/especialidades";
import { especialidades, nomesEspecialidades } from "../../servicos/especialidades";
import type { Usuario } from "../../servicos/autenticacao";

type ModalPerfilUsuarioProps = {
  usuario: Usuario | null;
  salvando: boolean;
  erro: string;
  onFechar: () => void;
  onLimparErro: () => void;
  onSalvar: (dados: {
    nome: string;
    telefone: string;
    especialidades?: Especialidade[];
  }) => Promise<boolean>;
};

type FormDataPerfil = {
  nome: string;
  telefone: string;
  especialidades: Especialidade[];
};

function criarFormData(usuario: Usuario | null): FormDataPerfil {
  return {
    nome: usuario?.nome ?? "",
    telefone: usuario?.telefone ?? "",
    especialidades: usuario?.especialidades ?? [],
  };
}

export default function ModalPerfilUsuario({
  usuario,
  salvando,
  erro,
  onFechar,
  onLimparErro,
  onSalvar,
}: ModalPerfilUsuarioProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState<FormDataPerfil>(() => criarFormData(usuario));
  const [mensagemSucesso, setMensagemSucesso] = useState("");

  const perfil = usuario;

  useEffect(() => {
    setFormData(criarFormData(usuario));
    setIsEditing(false);
  }, [usuario]);

  function alternarEspecialidade(especialidade: Especialidade) {
    if (!isEditing) return;
    setFormData((atual) => ({
      ...atual,
      especialidades: atual.especialidades.includes(especialidade)
        ? atual.especialidades.filter((item) => item !== especialidade)
        : [...atual.especialidades, especialidade],
    }));
  }

  function handleChange(event: ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  function iniciarEdicao() {
    onLimparErro();
    setMensagemSucesso("");
    setIsEditing(true);
  }

  function cancelarEdicao() {
    setFormData(criarFormData(usuario));
    setIsEditing(false);
    onLimparErro();
    setMensagemSucesso("");
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!isEditing || salvando) return;

    onLimparErro();
    setMensagemSucesso("");

    const { nome, telefone, especialidades: selecionadas } = formData;
    const salvo = await onSalvar({
      nome: nome.trim(),
      telefone: telefone.trim(),
      ...(perfil?.perfil === "PRESTADOR" ? { especialidades: selecionadas } : {}),
    });

    if (salvo) {
      setIsEditing(false);
      setMensagemSucesso("Perfil atualizado.");
    }
  }

  if (!perfil) return null;
  const desabilitado = !isEditing || salvando;

  return (
    <div
      className="dashboard-modal-backdrop"
      role="presentation"
      onMouseDown={(event) => {
        if (event.target === event.currentTarget && !salvando) onFechar();
      }}
    >
      <section
        className="dashboard-modal dashboard-profile-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="perfil-modal-titulo"
      >
        <header className="dashboard-modal-header">
          <div className="dashboard-profile-heading">
            <span className="dashboard-profile-icon">
              <UserRound size={20} aria-hidden="true" />
            </span>
            <div>
              <p className="dashboard-eyebrow">SEU CADASTRO</p>
              <h2 id="perfil-modal-titulo">Meu perfil</h2>
            </div>
          </div>
          <button
            className="dashboard-close"
            type="button"
            onClick={onFechar}
            disabled={salvando}
            aria-label="Fechar perfil"
          >
            <X size={21} aria-hidden="true" />
          </button>
        </header>

        <form onSubmit={(event) => void handleSubmit(event)}>
          <label className="dashboard-profile-field">
            <span>Nome</span>
            <input
              className="dashboard-field"
              name="nome"
              value={formData.nome}
              onChange={handleChange}
              disabled={desabilitado}
              required
            />
          </label>

          <label className="dashboard-profile-field">
            <span>E-mail</span>
            <input className="dashboard-field" name="email" value={perfil.email} disabled />
          </label>

          <label className="dashboard-profile-field">
            <span>Telefone</span>
            <input
              className="dashboard-field"
              type="tel"
              name="telefone"
              value={formData.telefone}
              onChange={handleChange}
              disabled={desabilitado}
              placeholder="Seu telefone"
            />
          </label>

          <div className="dashboard-profile-field">
            <span>Tipo de conta</span>
            <input
              className="dashboard-field"
              name="tipoPerfil"
              value={perfil.perfil === "PRESTADOR" ? "Prestador" : "Cliente"}
              disabled
            />
          </div>

          {perfil.perfil === "PRESTADOR" && (
            <fieldset className="dashboard-profile-specialties" disabled={desabilitado}>
              <legend>Especialidades</legend>
              <div className="dashboard-specialties">
                {especialidades.map((especialidade) => (
                  <label className="dashboard-specialty" key={especialidade}>
                    <input
                      type="checkbox"
                      checked={formData.especialidades.includes(especialidade)}
                      onChange={() => alternarEspecialidade(especialidade)}
                      disabled={desabilitado}
                    />
                    {nomesEspecialidades[especialidade]}
                  </label>
                ))}
              </div>
            </fieldset>
          )}

          {erro && <p className="dashboard-error" role="alert">{erro}</p>}
          {mensagemSucesso && <p className="dashboard-success" role="status">{mensagemSucesso}</p>}

          <div className="dashboard-modal-actions">
            {isEditing ? (
              <>
                <button
                  className="dashboard-button dashboard-button-secondary"
                  type="button"
                  onClick={cancelarEdicao}
                  disabled={salvando}
                >
                  Cancelar
                </button>
                <button className="dashboard-button" type="submit" disabled={salvando}>
                  <Save size={16} aria-hidden="true" />
                  {salvando ? "Salvando..." : "Salvar Alterações"}
                </button>
              </>
            ) : (
              <>
                <button
                  className="dashboard-button dashboard-button-secondary"
                  type="button"
                  onClick={onFechar}
                >
                  Fechar
                </button>
                <button
                  className="dashboard-button"
                  type="button"
                  onClick={(event) => {
                    event.preventDefault();
                    iniciarEdicao();
                  }}
                >
                  <Pencil size={16} aria-hidden="true" />
                  Editar Perfil
                </button>
              </>
            )}
          </div>
        </form>
      </section>
    </div>
  );
}