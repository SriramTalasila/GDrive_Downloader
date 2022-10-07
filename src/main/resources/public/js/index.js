$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/user",
        success: function (result) {
            console.log(result);
            $("#username").text(result.principal.fullName);
            $("#userpic").attr("src",result.principal.picture);
           
        },
        error: function (result, status) {
            console.log(result);
            
        }
    });
});

const evtSource = new EventSource("/event/resources/usage");

evtSource.onmessage = (event) => {
    //console.log(event.data);
    var downloads = JSON.parse(event.data);
   // console.log(typeof JSON.parse(event.data));
    if(downloads.length > 0){
        $("#downloadsTable > tbody").empty();
        for(var i =0 ; i< downloads.length;i++){
          //  console.log(downloads[i]);
            var markup = "<tr>";
            markup = markup+'<th scope="row">'+i+'</th>';
            markup = markup+'<td>'+downloads[i].fileName+'</td>'
            markup = markup+'<td>'+(downloads[i].size / 1024000).toFixed(2)+' MB</td>';
            markup = markup + '<td><div class="progress"><div class="progress-bar" role="progressbar" aria-label="Example with label" style="width: '+downloads[i].progress+'%;" aria-valuenow="'+downloads[i].progress+'" aria-valuemin="0" aria-valuemax="100">'+downloads[i].progress+'%</div></div></td>';
            markup = markup + '<td> '+downloads[i].speed+'MB/s</td>';
            markup = markup+'</tr>';
            $("#downloadsTable > tbody").append(markup)
        }

    }
}

var seletedFile ; 
$("#download").click(($event) => {
    $event.preventDefault();
    $('#overlay').fadeIn();
    var link = $("#downloadlink").val();
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/getfileinfo",
        data: JSON.stringify({ "url": link }),
        contentType: "application/json",
        success: function (result) {
            console.log(result);
            seletedFile = result;   
            $("#downloadFileName").text(result.name);
            $("#downloadFileSize").text((result.size / 1024000).toFixed(2) + "MB");
            $("#downloadFileType").text(result.mimeType);
            $("#downloadConfirmModel").modal("show");
            $('#overlay').fadeOut();
        },
        error: function (result, status) {
            console.log(result);
            $('#overlay').fadeOut();
        }
    });

})

$("#startDownload").click(($event) =>{
    console.log(seletedFile);
    $('#overlay').fadeIn();
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/startdownload",
        data: JSON.stringify(seletedFile),
        contentType: "application/json",
        success: function (result) {
            console.log(result);
            $('#overlay').fadeOut();
            $("#downloadConfirmModel").modal("hide");
        },
        error: function (result, status) {
            console.log(result);
            $('#overlay').fadeOut();
        }
    });
})