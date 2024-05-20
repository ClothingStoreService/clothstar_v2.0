const loginButton = document.getElementById("login-btn");

if (loginButton) {
    loginButton.addEventListener("click", (event) => {
        fetch("/v1/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: document.getElementById("email").value,
                password: document.getElementById("password").value,
            }),
        }).then((res) => {
            res.headers.forEach(console.log);
            alert(res.headers.get("Authorization"));
            const token = res.headers.get("Authorization");
            localStorage.setItem("ACCESS_TOKEN", token);
            location.replace("/");
        }).catch(() => {
            alert("ajax 호출 에러")
        });
    })
}