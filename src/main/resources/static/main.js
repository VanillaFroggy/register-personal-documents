function openCreationForm() {
    document.getElementById("creation-form").style.display = "block";
}

function setValueOfHiddenValueWithQueryParam(id, paramName) {
    document.getElementById(id).value = getQueryParamValue(paramName);
}

function getQueryParamValue(name) {
    return new URLSearchParams(window.location.search).get(name);
}

function closeCreationForm() {
    document.getElementById("creation-form").style.display = "none";
}

function sendJsonObject(event, form, url, method) {
    event.preventDefault();
    const formData = new FormData(form);
    const jsonObject = Object.fromEntries(formData.entries());
    fetch(url, {
        method: method,
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(jsonObject)
    }).then((response) => {
        if (response.redirected) {
            window.location.href = response.url;
        } else if (response.ok) {
            window.location.reload();
        } else {
            console.error("Error with form sending")
        }
    }).catch(error => console.error(error))
    return false;
}

function deleteEntity(url, id) {
    fetch(url + id, {method: "DELETE"})
        .then((response) => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                window.location.reload()
            } else {
                console.error("Error with form sending")
            }
        }).catch(error => console.error(error))
}