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

  $( function() {
      $('input[name="workStart-button"]').on('click',function(){
           if (confirm("出勤時間を打刻しますか？")){
              return true;
           } else {
               return false;
           }
       });
    });

  $( function() {
        $('input[name="workEnd-button"]').on('click',function(){
             if (confirm("退勤時間を打刻しますか？")){
                return true;
             } else {
                 return false;
             }
         });
      });

  $( function() {
          $('input[name="restStart-button"]').on('click',function(){
               if (confirm("休憩開始時間を打刻しますか？")){
                  return true;
               } else {
                   return false;
               }
           });
        });
  $( function() {
            $('input[name="restEnd-button"]').on('click',function(){
                 if (confirm("休憩終了時間を打刻しますか？")){
                    return true;
                 } else {
                     return false;
                 }
             });
          });