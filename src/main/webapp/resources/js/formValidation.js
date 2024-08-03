function validateUrl(url){
    if (url === '')
    {
        return true;
    }
    
    const pattern = /^(https?:\/\/)?([\w\d-]+\.)+[a-z]{2,}(\:[0-9]+)?(\/[\w\d-]+)*(\?[^\s]*)?$/i;
    return pattern.test(url);
}

function validateRows(rows){
	if (rows==='')
	{
		return true;
	}
    else if (isNaN(Number(rows)))
    {
		
        return false;
    }
    else if (Number(rows) > 5 || Number(rows) < 1)
	{		
        return false;
    }
    
    return true;
}


function validateForm(){
    var passedUrl = document.getElementById('formId:inputUrl').value;
    var passedRows = document.getElementById('formId:passedRows').value;
    
    if(!validateUrl(passedUrl))
    {
		alert('Invalid URL, should be https://mysite.com or https://mysite.com/.../screenshot');
        return false;
    }

    if(!validateRows(passedRows))
    {
        alert('Invalid row limitation provided, should be 1...5');
        return false;
    }
    
    PF('poll').start();
    
    alert('Processing started. Please allow up to 2 minutes.');
    
    return true;
}
