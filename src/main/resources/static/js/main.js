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

  $( function() {
    $('input[name="approval-button"]').on('click',function(){
         if (confirm("この勤怠を承認しますか？")){
            return true;
         } else {
             return false;
         }
     });
  });

    $( function() {
      $('input[name="remand-button"]').on('click',function(){
           if (confirm("この勤怠を差し戻ししますか？")){
              return true;
           } else {
               return false;
           }
       });
    });
