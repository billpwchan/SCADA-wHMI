const http = require("http")
,path = require("path")
,url = require("url")
,qs = require("querystring")
,fs = require("fs");

const port=8080;

const 
STR_FILEPATH="filepath"
,STR_METHOD="method"
,STR_DOWNLOAD="download"
,STR_STREAM="stream"
,STR_DATA="data"
,STR_STORAGE_FOLDER="storage";

const storagePath=__dirname+path.sep+STR_STORAGE_FOLDER;
const cmdPath=process.cwd();

/* response file*/
function loadFile(pathname,method,res) {
  console.log("pathname[",pathname,"] method[",method,"]");
  
  // Conver to fullpath
  let full_path = path.join(process.cwd(), STR_STORAGE_FOLDER, pathname);
  console.log("full_path[",full_path,"]");
  
  // Protect the access within the storage path
  let absolutePath=path.resolve(full_path);
  console.log("absolutePath[",absolutePath,"]");
  console.log("storagePath[",storagePath,"]");
  if(absolutePath.startsWith(storagePath)){
    fs.exists(full_path, function (exists) {
      if (!exists) {
        res.writeHeader(404, { "Content-Type": "text/plain" });
        res.write("404 Not Found\n");
        res.end();
      }
      else {
        fs.readFile(full_path, "binary", function (err, file) {
          if (err) {
            res.writeHeader(500, { "Content-Type": "text/plain" });
            res.write(err + "\n");
            res.end();
          }
          else {
            let base_name=path.basename(full_path);
            let ext_name=path.extname(full_path);
            console.log("base_name[",base_name,"] ext_name[",ext_name,"]");
            if ( method.toLowerCase() === STR_DOWNLOAD ) {
              /* response as file */
              res.setHeader('Content-disposition', 'attachment; filename='+base_name);
              res.writeHeader(200, {'Content-Type': 'text/csv'});
              res.write(file, "binary");
              res.end();
            } else {
              res.writeHeader(200);
              res.write(file, "binary");
              res.end();
            }
          }
        });
      }
    });
  } else {
    console.log("Outside storage path");
    //Outside storage path
    res.writeHeader(500, { "Content-Type": "text/plain" });
    res.write("outside storage path\n");
    res.end();
  }
}
function savefile(pathname,data,res) {
  console.log("pathname[",pathname,"] data[",data,"]");

  // Conver to fullpath
  let full_path = path.join(process.cwd(), STR_STORAGE_FOLDER, pathname);
  console.log("full_path[",full_path,"]");

  // Protect the access within the storage path
  let absolutePath=path.resolve(full_path);
  console.log("absolutePath[",absolutePath,"]");
  console.log("storagePath[",storagePath,"]");
  if(absolutePath.startsWith(storagePath)){
    fs.writeFile(full_path,data, function(err) {
      if(err) {
          console.log(err);
          res.writeHeader(500, { "Content-Type": "text/plain" });
          res.write(err + "\n");
          res.end();
      }
      console.log("The file was saved!");
      res.writeHeader(200, { "Content-Type": "text/plain" });
      res.write("done" + "\n");
      res.end();
    }); 
  } else {
    console.log("Outside storage path");
    //Outside storage path
    res.writeHeader(500, { "Content-Type": "text/plain" });
    res.write("outside storage path\n");
    res.end();
  }
}
function addAccessControlHeader(origin,res){
  // Website you wish to allow to connect
  res.setHeader('Access-Control-Allow-Origin', origin);
  
  // Request methods you wish to allow
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

  // Request headers you wish to allow
  res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

  // Set to true if you need the website to include cookies in the requests sent
  // to the API (e.g. in case you use sessions)
  res.setHeader('Access-Control-Allow-Credentials', true);
}
http.createServer(function(req,res){
  addAccessControlHeader('*',res);
  let reqmethod=req.method;
  console.log("reqmethod[",reqmethod,"]");
  let filepath, method;
  if(reqmethod==='POST') {
    var body='';
    req.on('data', function (data) {
        body +=data;
    });
    req.on('end',function(){
        var POST =  qs.parse(body);
        console.log(POST);
        filepath=POST[STR_FILEPATH];
        data=POST[STR_DATA];
        console.log("filepath[",filepath,"] data[",data,"]");
        if (null!=filepath&&null!=data){
          savefile(filepath,data,res);
        }
    });
  }
  else if(reqmethod==='GET') {
    let query=url.parse(req.url, true).query;
    console.log(query);
    filepath=query[STR_FILEPATH];
    method=query[STR_METHOD];
    console.log("filepath[",filepath,"] method[",method,"]");
    if(filepath!=null){
        loadFile(filepath,method,res); 
    } 
  }
}).listen(port);
console.log("Server Running on [",port,"]");  