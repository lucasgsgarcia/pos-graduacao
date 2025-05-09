const form = document.querySelector("form");

form.addEventListener("submit", function(event) {
  event.stopPropagation();
  event.preventDefault();

  console.log("Olá mundo!");

  return false;
});
