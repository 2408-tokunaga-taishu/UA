 $( function() {
   $('input[name="isStopped-button"]').on('click',function(){
        if (confirm("アカウント稼働状態を変更しますか？")){
           return true;
        } else {
            return false;
        }
    });
 });

 $( function() {
   $('input[name="delete-button"]').on('click',function(){
        if (confirm("本当に削除しますか？")){
           return true;
        } else {
            return false;
        }
    });
 });

  $( function() {
    $('input[name="request-button"]').on('click',function(){
         if (confirm("申請を行いますか？")){
            return true;
         } else {
             return false;
         }
     });
  });