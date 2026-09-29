FROM node:18-alpine

WORKDIR /app

# Copia e instala as dependências
COPY package*.json ./
RUN npm install

# Copia o código-fonte do frontend
COPY . .

EXPOSE 5173

# Para Vite use 'npm run dev -- --host', para CRA use 'npm start'
CMD ["npm", "run", "dev", "--", "--host"]