import React, { useState } from 'react';
import { Eye, EyeOff } from 'lucide-react';
import './InputSenha.css';

type InputSenhaProps = React.InputHTMLAttributes<HTMLInputElement>;

export function InputSenha(props: InputSenhaProps) {
  const [mostrarSenha, setMostrarSenha] = useState(false);

  return (
    <div className="input-senha-container">
      <input
        {...props}
        type={mostrarSenha ? 'text' : 'password'}
      />
      <button
        type="button"
        onClick={() => setMostrarSenha((prev) => !prev)}
        className="input-senha-toggle"
        aria-label={mostrarSenha ? 'Ocultar senha' : 'Exibir senha'}
      >
        {mostrarSenha ? <EyeOff size={20} /> : <Eye size={20} />}
      </button>
    </div>
  );
}