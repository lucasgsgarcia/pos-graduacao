const express = require('express');
const cors = require('cors');

const app = express();
const port = 3000;

app.use(cors());

const dataEvento = new Date('2025-06-12');

app.get('/evento', (req, res) => {
  const agora = new Date();
  let diferenca = dataEvento - agora;

  if (diferenca < 0) {
    diferenca = 0;
  }

  const dias = Math.floor(diferenca / (1000 * 60 * 60 * 24));
  const horas = Math.floor((diferenca / (1000 * 60 * 60)) % 24);
  const minutos = Math.floor((diferenca / (1000 * 60)) % 60);
  const segundos = Math.floor((diferenca / 1000) % 60);

  res.json({
    nome: 'Dia dos Namorados',
    dias,
    horas,
    minutos,
    segundos,
    encerrado: diferenca === 0
  });
});

app.listen(port, '0.0.0.0', () => {
  console.log(`Servidor rodando em http://localhost:${port}`);
});
