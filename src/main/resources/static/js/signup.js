const createButton = document.getElementById("create-btn");

if (createButton) {
    createButton.addEventListener("click", (event) => {
        fetch("/v1/members", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: document.getElementById("email").value,
                password: document.getElementById("password").value,
                name: document.getElementById("name").value,
                telNo: document.getElementById("telNo").value,
            }),
        }).then((res) => res.json())
            .then((res) => {
                if (res.success) {
                    alert(res.message);
                    location.replace("/login");
                } else {
                    alert(res.message);
                }
            }).catch(() => {
            alert("ajax 호출 에러")
        });
    })
}

const emailCheck = () => {
    const emailValue = document.getElementById("email").value;

    if (emailValue) {
        fetch(`/v1/members/email/${emailValue}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        }).then((res) => {
            return res.json()
        }).then((res) => {
            if (res.success) {
                alert(res.message);
            } else {
                alert(res.message);
            }
        }).catch(() => {
            console.log("catch");
        });
    }
}