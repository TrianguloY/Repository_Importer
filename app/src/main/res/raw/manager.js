/*
Script necessary for the repository importer to work correctly. DO NOT delete it unless you uninstalled first the Repository importer app.
Don't change the name of this script, it will allow to update it without creating another one
*/

//IMPORTANT: don't change this variables
var version = 1;
var separator = "$%&";


var data=LL.getEvent().getData()
if(data!=null)data=data.split(separator);

var intent=new Intent("android.intent.action.MAIN");
intent.setClassName("com.trianguloy.llscript.repository","com.trianguloy.llscript.repository.webviewer");


if(data==null){
    //Send the id to the importer app
    intent.putExtra("id",LL.getCurrentScript().getId());
}else{
    //Data received. Let's assume it is correct
    if(data[0]!=version){
        //outdated version
        alert("The script manager is outdated. Please reimport it from the scripts menu");
        return;
    }

    var toast="";
    var scripts=LL.getAllScriptMatching(Script.FLAG_ALL);
    var match=null;
    for(var t=0;t<scripts.getLength();++t){
    if(scripts.getAt(t).getName()==data[1])match=scripts.getAt(t);
    //if duplicated, only the last one (oldest in most cases)
    }

    if(match==null){
        //Not found. Create
        LL.createScript(data[1],data[2],parseInt(data[3]));
        toast="Script imported successfully.\nAvailable in the launcher";
    }else if(match.getText()==data[2]){
        //same name and code
        toast="Script already imported, nothing changed";
    }else{
        //same name, different code
        if(confirm("There is a script with the same name but different code. Do you want to update it?")){
            //update
            match.setText(data[2]);
            toast="Script updated";
        }else{
            //don't update
            toast="Not imported";
        }
    }

    //alert(data[1]+"\n\n"+data[2]+"\n\n"+data[3]);

    Android.makeNewToast(toast, true).show();
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
}

LL.startActivity(intent)