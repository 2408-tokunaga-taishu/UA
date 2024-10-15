
 $( function() {
   $('input[name="isStopped-button"]').on('click',function(){
        if (confirm("アカウント稼働状態を変更しますか？")){
           return true;
        } else {
            return false;
        }
    });
 });