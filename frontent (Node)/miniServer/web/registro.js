window.onload = function () {
    document.getElementById("submit").addEventListener("click", register);
};

async function register() {
    if ($("#id").val() != "" && $("#email").val().match('[\w]*[@][\w]*') && $("#name").val() != "" && $("#password").val() != "" && $("#birthdate").val() != "") {
        let user = {
            id: $("#id").val(),
            name: $("#name").val(),
            email: $("#email").val(),
            birthDate: $("#birthdate").val(),
            password: $("#password").val(),
            country: { name: $("#countrySelect").val() }
        }
        let promise = await fetch('http://localhost:8080/tourApi/putUser', {
            method: "POST",
            body: JSON.stringify(user),
            headers: {
                'Content-Type': 'application/json'
            }
        })

        if(await promise.text()==="true"){
            let promise = await fetch("http://localhost:3000/login?username="+user.email+"&password="+user.password)
        }
    }
}