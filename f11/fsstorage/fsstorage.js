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
function loadFile(pathname,res) {
  console.log("loadFile pathname[",pathname,"]");
  
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
        // File Not Found
        res.writeHeader(404, { "Content-Type": "text/plain" });
        res.write("404 Not Found\n");
        res.end();
      }
      else {
        fs.readFile(full_path, "binary", function (err, file) {
          if (!err) {
            // Response with data
            let base_name=path.basename(full_path);
            let ext_name=path.extname(full_path);
            console.log("base_name[",base_name,"] ext_name[",ext_name,"]");

            let json = {};
            json['filepath'] = base_name;
            json['data'] = file;

            res.writeHead(200, {"Content-Type": "application/json"});
            res.end(JSON.stringify(json));
          }
          else {
            // Reading Error
            res.writeHead(500, {"Content-Type": "application/json"});
            let json = {};
            json['filepath'] = base_name;
            json['data'] = err;
            res.write(JSON.stringify(json) + "\n");
            res.end();
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
  console.log("savefile pathname[",pathname,"] data[",data,"]");

  // Conver to fullpath
  let full_path = path.join(process.cwd(), STR_STORAGE_FOLDER, pathname);
  console.log("full_path[",full_path,"]");

  // Protect the access within the storage path
  let absolutePath=path.resolve(full_path);
  console.log("absolutePath[",absolutePath,"]");
  console.log("storagePath[",storagePath,"]");
  if(absolutePath.startsWith(storagePath)){
    fs.writeFile(full_path,data, function(err) {
      if(!err) {
        console.log("The file was saved!");
        res.writeHead(200, {"Content-Type": "application/json"});
        let json = {};
        json['filepath'] = base_name;
        json['data'] = 'done';
        res.write(JSON.stringify(json) + "\n");
        res.end();        
      } else {
        // Write File Error
        res.writeHead(500, {"Content-Type": "application/json"});
        let json = {};
        json['filepath'] = base_name;
        json['data'] = err;
        res.write(JSON.stringify(json) + "\n");
        res.end();     
      }
    }); 
  } else {
    console.log("Outside storage path");
    //Outside storage path
    res.writeHead(500, {"Content-Type": "application/json"});
    let json = {};
    json['filepath'] = base_name;
    json['data'] = 'Outside storage path';
    res.write(JSON.stringify(json) + "\n");
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

function resOptions(res){
  var headers = {};
  // IE8 does not allow domains to be specified, just the *
  // headers["Access-Control-Allow-Origin"] = req.headers.origin;
  headers["Access-Control-Allow-Origin"] = "*";
  headers["Access-Control-Allow-Methods"] = "POST, GET, PUT, DELETE, OPTIONS";
  headers["Access-Control-Allow-Credentials"] = false;
  headers["Access-Control-Max-Age"] = '86400'; // 24 hours
  headers["Access-Control-Allow-Headers"] = "X-Requested-With, X-HTTP-Method-Override, Content-Type, Accept";
  res.writeHead(200, headers);
  res.end();
}

// Entry Point
http.createServer(function(req,res){
  addAccessControlHeader('*',res);
  let reqmethod=req.method;
  console.log("reqmethod[",reqmethod,"]");
  let filepath, method;
  
  if(reqmethod==='OPTIONS') {
    resOptions(res);
  }
  else if(reqmethod==='POST' || reqmethod==='PUT' ) {

    let data = "";
    // req.on("readable", text => data += text);
    req.on('data', text => {
      // console.error("data data["+data+"]");
      data += text
    });
    req.on("end", () => {
      // console.error("end data["+data+"]");
      try {
        const json = JSON.parse(data);
        // console.log("JSON["+json+"]");

        filepath=json[STR_FILEPATH];
        data=json[STR_DATA];
        console.log("filepath[",filepath,"] data[",data,"]");

        if (null!=filepath&&null!=data){
          savefile(filepath,data,res);
        }

      }
      catch (err) {
        console.error("request body was not JSON");
        console.error("err["+err+"]");
      }
    });
  }
  else if(reqmethod==='GET') {
    let query=url.parse(req.url, true).query;
    console.log("query[",query,"]");
    filepath=query[STR_FILEPATH];
    console.log("filepath[",filepath,"]");
    if(filepath!=null){
      loadFile(filepath,res);
    }
  }
  else {
    res.writeHead(404);
    res.end();
  }
}).listen(port);
console.log("Server Running on [",port,"]");