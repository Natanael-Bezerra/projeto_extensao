// Função para adicionar campos de turmas e professores dinamicamente
document.getElementById('num-classes').addEventListener('input', function () {
    const numClasses = parseInt(this.value);
    const classSection = document.getElementById('class-section');
    classSection.innerHTML = ''; // Limpa a seção de turmas

    for (let i = 1; i <= numClasses; i++) {
        const classDiv = document.createElement('div');
        classDiv.classList.add('class-div');
        classDiv.innerHTML = `
            <h3>Turma ${i}</h3>
            <label for="class-${i}-name">Nome da Turma:</label>
            <input type="text" id="class-${i}-name" name="class-${i}-name" required>

            <label for="class-${i}-teachers">Número de Professores:</label>
            <input type="number" id="class-${i}-teachers" name="class-${i}-teachers" required>

            <div id="teachers-${i}"></div>
        `;
        classSection.appendChild(classDiv);

        // Evento para adicionar professores
        const numTeachersInput = classDiv.querySelector(`#class-${i}-teachers`);
        numTeachersInput.addEventListener('input', function () {
            const numTeachers = parseInt(this.value);
            const teachersSection = classDiv.querySelector(`#teachers-${i}`);
            teachersSection.innerHTML = ''; // Limpa a lista de professores

            for (let j = 1; j <= numTeachers; j++) {
                const teacherDiv = document.createElement('div');
                teacherDiv.classList.add('teacher-div');
                teacherDiv.innerHTML = `
                    <h4>Professor ${j}</h4>
                    <label for="teacher-${i}-${j}-name">Nome do Professor:</label>
                    <input type="text" id="teacher-${i}-${j}-name" name="teacher-${i}-${j}-name" required>

                    <div>
                        <label for="teacher-${i}-${j}-monday">Aulas Disponíveis na Segunda:</label>
                        <input type="text" id="teacher-${i}-${j}-monday" name="teacher-${i}-${j}-monday" placeholder="1,3,5">

                        <label for="teacher-${i}-${j}-tuesday">Aulas Disponíveis na Terça:</label>
                        <input type="text" id="teacher-${i}-${j}-tuesday" name="teacher-${i}-${j}-tuesday" placeholder="2,4">

                        <label for="teacher-${i}-${j}-wednesday">Aulas Disponíveis na Quarta:</label>
                        <input type="text" id="teacher-${i}-${j}-wednesday" name="teacher-${i}-${j}-wednesday" placeholder="1,2">

                        <label for="teacher-${i}-${j}-thursday">Aulas Disponíveis na Quinta:</label>
                        <input type="text" id="teacher-${i}-${j}-thursday" name="teacher-${i}-${j}-thursday" placeholder="3,4">

                        <label for="teacher-${i}-${j}-friday">Aulas Disponíveis na Sexta:</label>
                        <input type="text" id="teacher-${i}-${j}-friday" name="teacher-${i}-${j}-friday" placeholder="1,5">
                    </div>
                `;
                teachersSection.appendChild(teacherDiv);
            }
        });
    }
});

// Função para coletar os dados do formulário e enviá-los como JSON
document.getElementById('scheduleForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const formData = new FormData(this);

    // Estrutura do JSON
    const jsonData = {
        horario: [
            formData.get('start-time'),
            parseInt(formData.get('lesson-duration')),
            parseInt(formData.get('lessons-before-break')),
            parseInt(formData.get('break-duration'))
        ],
        diasAula: {
            segunda: parseInt(formData.get('monday-classes') || 0),
            terca: parseInt(formData.get('tuesday-classes') || 0),
            quarta: parseInt(formData.get('wednesday-classes') || 0),
            quinta: parseInt(formData.get('thursday-classes') || 0),
            sexta: parseInt(formData.get('friday-classes') || 0)
        },
        turmas: []
    };

    // Processar as turmas e professores
    const numClasses = parseInt(formData.get('num-classes'));
    for (let i = 1; i <= numClasses; i++) {
        const turma = {
            nomeTurma: formData.get(`class-${i}-name`),
            professores: []
        };

        const numTeachers = parseInt(formData.get(`class-${i}-teachers`));
        for (let j = 1; j <= numTeachers; j++) {
            const professor = {
                nomeProfessor: formData.get(`teacher-${i}-${j}-name`),
                disponibilidade: {}
            };

            // Adicionar disponibilidade por dia
            ['monday', 'tuesday', 'wednesday', 'thursday', 'friday'].forEach((day, index) => {
                const aulas = formData.get(`teacher-${i}-${j}-${day}`);
                if (aulas) {
                    professor.disponibilidade[day] = aulas.split(',').map(aula => parseInt(aula.trim()));
                }
            });

            turma.professores.push(professor);
        }

        jsonData.turmas.push(turma);
    }

    console.log(JSON.stringify(jsonData, null, 2));
    alert('Formulário enviado com sucesso! Veja os dados no console.');
});