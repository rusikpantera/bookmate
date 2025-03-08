document.addEventListener("DOMContentLoaded", function () {
    let authors = [];

    fetch("http://localhost:8080/api/authors")
        .then(response => response.json())
        .then(data => {
            authors = data;
        })
        .catch(error => console.error("Ошибка загрузки авторов:", error));

    function filterAuthors() {
        const input = document.getElementById("author");
        const filter = input.value.toLowerCase();
        const authorList = document.getElementById("author-list");

        authorList.innerHTML = "";

        authors.forEach(author => {
            if (author.surname.toLowerCase().includes(filter) || author.name.toLowerCase().includes(filter) || author.patronymic.toLowerCase().includes(filter)) {
                const fullName = author?.surname + ' ' + author?.name + ' ' + author?.patronymic;
                authorList.innerHTML += `<div class="author-option" onclick="selectAuthor('${author.id}', '${author.surname}', '${author.name}')">${fullName}</div>`;
            }
        });

        authorList.style.display = authorList.innerHTML.trim() === "" ? "none" : "block";
    }

    function selectAuthor(authorId, surname, name) {
        const input = document.getElementById("author");
        const authorList = document.getElementById("author-list");

        input.value = `${surname} ${name}`;
        document.getElementById("author-id").value = authorId;

        authorList.style.display = "none";
    }

    document.getElementById("author").addEventListener("focus", function () {
        filterAuthors();
    });

    document.addEventListener("click", function (event) {
        const input = document.getElementById("author");
        const authorList = document.getElementById("author-list");


        if (event.target !== input && !authorList.contains(event.target)) {
            authorList.style.display = "none";
        }
    });

    window.filterAuthors = filterAuthors;
    window.selectAuthor = selectAuthor;
});
let authors = [];

function loadAuthors() {
    fetch("http://localhost:8080/api/authors")
        .then(response => response.json())
        .then(data => {
            console.log("Загруженные авторы:", data);
            authors = data;
        })
        .catch(error => console.error("Ошибка загрузки авторов:", error));
}

document.addEventListener("DOMContentLoaded", loadAuthors);

