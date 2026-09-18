import { type FormEvent } from "react";

export default function Cadastro() {
	function handleSubmit(event: FormEvent<HTMLFormElement>) {
		event.preventDefault();
	}

	return (
		<main>
			<h1>Cadastro</h1>
			<p>Crie sua conta no ServiceHub.</p>

			<form onSubmit={handleSubmit}>
				<div>
					<label htmlFor="nome">Nome</label>
					<input id="nome" name="nome" type="text" required />
				</div>

				<div>
					<label htmlFor="email">E-mail</label>
					<input id="email" name="email" type="email" required />
				</div>

				<div>
					<label htmlFor="senha">Senha</label>
					<input id="senha" name="senha" type="password" required />
				</div>

				<button type="submit">Cadastrar</button>
			</form>
		</main>
	);
}
