async function buscarContagem() {
  try {
    const resposta = await fetch('http://localhost:3000/evento');
    const dados = await resposta.json();
    return dados;
  } catch (error) {
    console.error('Erro ao buscar dados:', error);
    return null;
  }
}

function atualizarTela({ dias, horas, minutos, segundos, encerrado }) {
  document.getElementById('dias').textContent = String(dias).padStart(2, '0');
  document.getElementById('horas').textContent = String(horas).padStart(2, '0');
  document.getElementById('minutos').textContent = String(minutos).padStart(2, '0');
  document.getElementById('segundos').textContent = String(segundos).padStart(2, '0');

  if (encerrado) {
    clearInterval(intervalo);
  }
}

async function atualizar() {
  const dados = await buscarContagem();
  if (dados) {
    atualizarTela(dados);
  }
}

const intervalo = setInterval(atualizar, 1000);
atualizar();

