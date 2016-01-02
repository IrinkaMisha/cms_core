var injeditor={
    init:function(){
        var txtEd=$('.textEdit');
        this.loadCKEditor();
        //if(txtEd.length>0){
        //    this.loadCKEditor();
        //}
    },
    loadCKEditor:function(){
        if(document.getElementById('scriptckeditor')==null){
            var script = document.createElement('script');
            script.type = 'text/javascript';
            script.src = '/webjars/ckeditor/4.4.7-1/full/ckeditor.js';
            script.id = 'scriptckeditor';
            document.getElementsByTagName('head')[0].appendChild(script);
        }else{
            ckeditorload();
        }
    }
}

//При замене версии CKEDITOR в конец файла ckeditor.js поместить выpов этой функции. ckeditorload();
function ckeditorload(){
    //CKEDITOR.basePath = '/webjars/ckeditor/4.4.7-1/full/';
    CKEDITOR.config.allowedContent=true;
    $('.textEdit').each(function(ind,el){
        //<![CDATA[
        CKEDITOR.replace($(el).attr('id'));
        //]]>
    });
}

$(function(){
    injeditor.init();
});