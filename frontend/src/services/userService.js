const apiUrl = "http://localhost:8080/api/user"


export async function findUserById(id) {
    
    const response = await fetch(apiUrl + "/" + id, {
        method: "GET"
    });

    if(!response.ok){
        const error = await response.json();
        throw Error(error.message);

    }

    const data = await response.json();

    return data;

}


export async function saveUser(user) {


    const response = await fetch(apiUrl, {

        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify(user)
    });

    const data = await response.json();

    return data;
    
}
