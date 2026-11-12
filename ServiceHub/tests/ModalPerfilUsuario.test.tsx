// @vitest-environment jsdom
import "@testing-library/jest-dom/vitest";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { useState } from "react";
import type { ComponentProps } from "react";
import { describe, expect, it, vi } from "vitest";
import ModalPerfilUsuario from "../src/componentes/modal/ModalPerfilUsuario";
import type { Usuario } from "../src/servicos/autenticacao";

const prestador: Usuario = {
  id: 21,
  nome: "Maria Prestadora",
  email: "maria@example.com",
  perfil: "PRESTADOR",
  telefone: "(48) 99999-0000",
  especialidades: ["ELETRICISTA"],
  endereco: {
    cep: "88000000",
    logradouro: "Rua Central",
    numero: "10",
    complemento: "Casa",
    bairro: "Centro",
    cidade: "Florianópolis",
    estado: "SC",
  },
};

function renderModal(overrides: Partial<ComponentProps<typeof ModalPerfilUsuario>> = {}) {
  const onSalvar = vi.fn().mockResolvedValue(true);
  const onLimparErro = vi.fn();
  render(
    <ModalPerfilUsuario
      usuario={prestador}
      salvando={false}
      erro=""
      onFechar={vi.fn()}
      onSalvar={onSalvar}
      onLimparErro={onLimparErro}
      {...overrides}
    />,
  );
  return { onSalvar, onLimparErro };
}

describe("ModalPerfilUsuario", () => {
  it("abre com os campos desabilitados e o botão Editar Perfil", () => {
    renderModal();

    expect(screen.getByRole("button", { name: "Editar Perfil" })).toBeEnabled();
    for (const nome of ["Nome", "Telefone"]) {
      expect(screen.getByLabelText(nome)).toBeDisabled();
    }
    expect(screen.queryByLabelText(/CEP|Rua|Número|Complemento|Bairro|Cidade|Estado/)).not.toBeInTheDocument();
    expect(screen.getByLabelText("E-mail")).toBeDisabled();
    expect(screen.getByDisplayValue("Prestador")).toBeDisabled();
  });

  it("habilita somente os campos permitidos ao entrar em edição", () => {
    renderModal();

    fireEvent.click(screen.getByRole("button", { name: "Editar Perfil" }));

    for (const nome of ["Nome", "Telefone"]) {
      expect(screen.getByLabelText(nome)).toBeEnabled();
    }
    expect(screen.queryByLabelText(/CEP|Rua|Número|Complemento|Bairro|Cidade|Estado/)).not.toBeInTheDocument();
    expect(screen.getByLabelText("E-mail")).toBeDisabled();
    expect(screen.getByDisplayValue("Prestador")).toBeDisabled();
    expect(screen.getByLabelText("Eletricista")).toBeEnabled();
    expect(screen.getByRole("button", { name: "Salvar Alterações" })).toBeEnabled();
  });

  it("envia o payload editado limpo e oculta sucesso ao voltar a editar", async () => {
    const { onSalvar } = renderModal();
    fireEvent.click(screen.getByRole("button", { name: "Editar Perfil" }));
    fireEvent.change(screen.getByLabelText("Nome"), { target: { value: "  Maria Nova  " } });
    fireEvent.change(screen.getByLabelText("Telefone"), { target: { value: "  48988887777  " } });
    fireEvent.click(screen.getByLabelText("Pintor"));
    fireEvent.click(screen.getByRole("button", { name: "Salvar Alterações" }));

    await waitFor(() => {
      expect(onSalvar).toHaveBeenCalledWith({
        nome: "Maria Nova",
        telefone: "48988887777",
        especialidades: ["ELETRICISTA", "PINTOR"],
      });
    });
    expect(await screen.findByRole("status")).toHaveTextContent("Perfil atualizado.");

    fireEvent.click(screen.getByRole("button", { name: "Editar Perfil" }));
    expect(screen.queryByRole("status")).not.toBeInTheDocument();
  });

  it("mantém a confirmação após sincronizar os dados retornados pela API", async () => {
    function ModalSincronizado() {
      const [usuario, setUsuario] = useState(prestador);
      return (
        <ModalPerfilUsuario
          usuario={usuario}
          salvando={false}
          erro=""
          onFechar={vi.fn()}
          onLimparErro={vi.fn()}
          onSalvar={async (dados) => {
            setUsuario((atual) => ({
              ...atual,
              nome: dados.nome,
              telefone: dados.telefone,
              especialidades: dados.especialidades ?? atual.especialidades,
            }));
            return true;
          }}
        />
      );
    }

    render(<ModalSincronizado />);
    fireEvent.click(screen.getByRole("button", { name: "Editar Perfil" }));
    fireEvent.change(screen.getByLabelText("Nome"), { target: { value: "Maria Atualizada" } });
    fireEvent.click(screen.getByRole("button", { name: "Salvar Alterações" }));

    expect(await screen.findByRole("status")).toHaveTextContent("Perfil atualizado.");
    expect(screen.getByLabelText("Nome")).toHaveValue("Maria Atualizada");
  });

  it("limpa erro anterior ao entrar novamente no modo de edição", () => {
    const onLimparErro = vi.fn();
    function ModalComErro() {
      const [erro, setErro] = useState("Falha anterior");
      return (
        <ModalPerfilUsuario
          usuario={prestador}
          salvando={false}
          erro={erro}
          onFechar={vi.fn()}
          onSalvar={vi.fn().mockResolvedValue(false)}
          onLimparErro={() => {
            onLimparErro();
            setErro("");
          }}
        />
      );
    }

    render(<ModalComErro />);
    expect(screen.getByRole("alert")).toHaveTextContent("Falha anterior");

    fireEvent.click(screen.getByRole("button", { name: "Editar Perfil" }));

    expect(onLimparErro).toHaveBeenCalled();
    expect(screen.queryByRole("alert")).not.toBeInTheDocument();
  });

  it("cancela edição sem enviar alterações", () => {
    const { onSalvar } = renderModal();
    fireEvent.click(screen.getByRole("button", { name: "Editar Perfil" }));
    fireEvent.change(screen.getByLabelText("Nome"), { target: { value: "Nome temporário" } });
    fireEvent.click(screen.getByRole("button", { name: "Cancelar" }));

    expect(screen.getByLabelText("Nome")).toHaveValue("Maria Prestadora");
    expect(screen.getByLabelText("Nome")).toBeDisabled();
    expect(onSalvar).not.toHaveBeenCalled();
  });
});
