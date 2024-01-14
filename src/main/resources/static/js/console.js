function uploadFile(){
    const file = document.getElementById("datafile").files[0];
    const dataBaseId = document.getElementById("databaseId").value

    if(file === null){
        alert("File cannot be empty")
        return;
    }

    const url =  window.location.origin + "/api/v1/import/file?dataBaseId=" + dataBaseId 

    let formData = new FormData();
    formData.append("datafile" , file);

    try{
        fetch(url, {
            method:"POST",
            body: formData
        }).then(response => {
            if(response.status === 200){
                alert("Data uploaded successfully")
                window.location.reload()
            }

            if(response.status === 409){
                alert("Data upload failed, check browser console for more details")
                response.json().then(body => console.log(body))
            }

        }).catch(error => {
            alert("Error while updating data");
        })

    
    }catch(e){
        console.log(e)
    }

}

function uploadContent(){
   let content =  document.getElementById('dataContent').value

   if(content === null){
        alert("Input is Empty");
        return;
   }

   const dataBaseId = document.getElementById("databaseId").value
   const url = window.location.origin + "/api/v1/import/content?dataBaseId=" + dataBaseId;

   var requestData = JSON.stringify({"content" : content})

   try{
        fetch(url , {
            method : "POST",
            headers: { "Content-Type": "application/json" },
            body : requestData,
            mode : "same-origin"
        }).then(response => {

            if(response.status === 200){
                alert("Data uploaded successfully")
                window.location.reload()
            }

            if(response.status === 409){
                alert("Data upload failed, check browser console for more details")
                response.json().then(body => console.log(body))
            }

        }).catch(error => {
            alert("Error while updating data")
        })

   }catch(e){
    console.log(e)
   }

   
}
