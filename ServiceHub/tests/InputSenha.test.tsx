// @vitest-environment jsdom
import "@testing-library/jest-dom/vitest";
import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { InputSenha } from "../src/componentes/InputSenha";

function renderInput(props: Partial<React.ComponentProps<typeof InputSenha>> = {}) {
  const onChange = vi.fn();
  render(<InputSenha aria-label="Senha" defaultValue="" onChange={onChange} {...props} />);
  return { onChange };
}

describe("InputSenha", () => {
  it("começa mascarado e com rótulo de ação para exibir", () => {
    renderInput();

    expect(screen.getByLabelText("Senha")).toHaveAttribute("type", "password");
    expect(screen.getByRole("button", { name: "Exibir senha" })).toBeEnabled();
  });

  it("alterna entre texto e senha ao clicar no botão", () => {
    renderInput();

    fireEvent.click(screen.getByRole("button", { name: "Exibir senha" }));
    expect(screen.getByLabelText("Senha")).toHaveAttribute("type", "text");
    expect(screen.getByRole("button", { name: "Ocultar senha" })).toBeInTheDocument();

    fireEvent.click(screen.getByRole("button", { name: "Ocultar senha" }));
    expect(screen.getByLabelText("Senha")).toHaveAttribute("type", "password");
    expect(screen.getByRole("button", { name: "Exibir senha" })).toBeInTheDocument();
  });

  it("mantém o botão fora do fluxo de submit do formulário", () => {
    renderInput();

    expect(screen.getByRole("button", { name: "Exibir senha" })).toHaveAttribute("type", "button");
  });

  it("repassa atributos e o evento de digitação ao input", () => {
    const { onChange } = renderInput({ placeholder: "Digite sua senha", disabled: false });

    const input = screen.getByPlaceholderText("Digite sua senha");
    fireEvent.change(input, { target: { value: "senha-segura" } });

    expect(onChange).toHaveBeenCalledTimes(1);
    expect(input).toHaveValue("senha-segura");
  });

  it("preserva o tipo quando desabilitado e não permite foco no botão", () => {
    renderInput({ disabled: true });

    expect(screen.getByLabelText("Senha")).toBeDisabled();
    expect(screen.getByRole("button", { name: "Exibir senha" })).toBeInTheDocument();
  });
});
