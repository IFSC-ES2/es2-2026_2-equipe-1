function Login() {
    return (
        <div>
            <h1>Login</h1>
            <form>
                <label htmlFor="username">Nome de usuário:</label>
                <input type="text" id="username" name="username" required />
                <br />
                <label htmlFor="password">Senha:</label>
                <input type="password" id="password" name="password" required />
                <br />
                <button type="submit">Entrar</button>
            </form>
        </div>
    );
}

export default Login;