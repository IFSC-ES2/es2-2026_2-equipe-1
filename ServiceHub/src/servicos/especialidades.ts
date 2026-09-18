export const especialidades = [
  "ELETRICISTA",
  "ENCANADOR",
  "PEDREIRO",
  "TECNICO_REDES",
  "PINTOR",
  "DIARISTA",
  "MARCENEIRO",
  "JARDINAGEM",
  "CHAVEIRO",
  "MONTADOR_MOVEIS",
] as const;

export type Especialidade = (typeof especialidades)[number];

export const nomesEspecialidades: Record<Especialidade, string> = {
  ELETRICISTA: "Eletricista",
  ENCANADOR: "Encanador",
  PEDREIRO: "Pedreiro",
  TECNICO_REDES: "Técnico de redes",
  PINTOR: "Pintor",
  DIARISTA: "Diarista",
  MARCENEIRO: "Marceneiro",
  JARDINAGEM: "Jardinagem",
  CHAVEIRO: "Chaveiro",
  MONTADOR_MOVEIS: "Montador de móveis",
};
