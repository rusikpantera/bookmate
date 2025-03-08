document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('modal');
    const closeButton = document.querySelector('.close-button');
    const authorName = document.getElementById('author-name');
    const authorBirthyear = document.getElementById('author-birthyear');
    const authorPhoto = document.getElementById('author-photo');

    let currentAuthor = null;

    document.querySelectorAll('.author-row').forEach(row => {
        row.addEventListener('click', function() {
            const name = this.getAttribute('data-name');
            const surname = this.getAttribute('data-surname');
            const patronymic = this.getAttribute('data-patronymic') || '';
            const birthyear = this.getAttribute('data-birthyear');
            const photo = this.getAttribute('data-photo');

            if (currentAuthor !== this) {
                currentAuthor = this;
                authorName.textContent = `${surname} ${name} ${patronymic}`;
                authorBirthyear.textContent = `Дата рождения: ${birthyear}`;
                authorPhoto.src = `${photo}`;
                modal.style.display = 'block';
            }
        });
    });
    closeButton.addEventListener('click', function() {
        modal.style.display = 'none';
    });
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});