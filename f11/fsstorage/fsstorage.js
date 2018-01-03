const http = require("http")
,path = require("path")
,url = require("url")
,qs = require("querystring")
,fs = require("fs");

const port=8080;

const
STR_OPERATION="operation"
,STR_GET_FILE="getfile"
,STR_GET_FILELIST="getfilelist"
,STR_DELETE_FILE="deletefile"
,STR_PATH="path"
,STR_METHOD="method"
,STR_DOWNLOAD="download"
,STR_STREAM="stream"
,STR_DATA="data"
,STR_STORAGE_FOLDER="storage";

const STR_CONTENT_TYPE = 'Content-Type';
const STR_CONTENT_TYPE_DATA = 'application/json; charset=utf-8';

const storagePath=__dirname+path.sep+STR_STORAGE_FOLDER;
const cmdPath=process.cwd();

/* response file*/
function getFileList(pathname,res) {
  console.log("getFileList pathname[",pathname,"]");
  
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
        res.writeHeader(404, { STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA });
        let json = {};
        json['path'] = pathname;
        json['data'] = '404 Not Found';
        res.write(JSON.stringify(json) + "\n");
        res.end(); 
      }
      else {
        fs.readdir(full_path, function (err, items) {
          // console.log('data[',data,']');

          if (!err) {
            // Response with data
            console.log("pathname[",pathname,"]");

            let json = {};
            json['path'] = pathname;
            console.log(items);
            json['data'] = [];
            for (var i=0; i<items.length; i++) {
              console.log(items[i]);
              json['data'].push(items[i]);
            }

            res.writeHead(200, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
            res.end(JSON.stringify(json));
          }
          else {
            // Reading Error
            res.writeHead(500, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
            let json = {};
            json['path'] = pathname;
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
    res.writeHeader(500, { STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA });
    let json = {};
    json['path'] = pathname;
    json['data'] = 'Outside storage path';
    res.write(JSON.stringify(json) + "\n");
    res.end(); 
  }
}

function deleteFile(pathname,res) {
  console.log("deleteFile pathname[",pathname,"]");
  
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
        res.writeHeader(404, { STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA });
        let json = {};
        json['path'] = pathname;
        json['data'] = '404 Not Found';
        res.write(JSON.stringify(json) + "\n");
        res.end(); 
      }
      else {
        fs.unlink(full_path, function (err) {
          // console.log('data[',data,']');

          if (!err) {
            // Response with data
            console.log("pathname[",pathname,"]");

            let json = {};
            json['path'] = pathname;
            json['data'] = 'OK';

            res.writeHead(200, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
            res.end(JSON.stringify(json));
          }
          else {
            // Reading Error
            res.writeHead(500, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
            let json = {};
            json['path'] = pathname;
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
    res.writeHeader(500, { STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA });
    let json = {};
    json['path'] = pathname;
    json['data'] = 'Outside storage path';
    res.write(JSON.stringify(json) + "\n");
    res.end(); 
  }
}

/* response file*/
function readFile(pathname,res) {
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
        res.writeHeader(404, { STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA });
        let json = {};
        json['path'] = pathname;
        json['data'] = '404 Not Found';
        res.write(JSON.stringify(json) + "\n");
        res.end(); 
      }
      else {
        fs.readFile(full_path, 'utf8', function (err, data) {
          // console.log('data[',data,']');

          if (!err) {
            // Response with data
            let base_name=path.basename(full_path);
            let ext_name=path.extname(full_path);
            console.log("base_name[",base_name,"] ext_name[",ext_name,"]");

            let json = {};
            json['path'] = base_name;
            json['data'] = data;

            res.writeHead(200, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
            res.end(JSON.stringify(json));
          }
          else {
            // Reading Error
            res.writeHead(500, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
            let json = {};
            json['path'] = base_name;
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
    res.writeHeader(500, { STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA });
    let json = {};
    json['path'] = pathname;
    json['data'] = 'Outside storage path';
    res.write(JSON.stringify(json) + "\n");
    res.end(); 
  }
}

function savefile(pathname,data,res) {
  console.log("savefile pathname[",pathname,"]");

  // Conver to fullpath
  let full_path = path.join(process.cwd(), STR_STORAGE_FOLDER, pathname);
  console.log("full_path[",full_path,"]");

  // Protect the access within the storage path
  let absolutePath=path.resolve(full_path);
  console.log("absolutePath[",absolutePath,"]");
  console.log("storagePath[",storagePath,"]");
  if(absolutePath.startsWith(storagePath)){
    fs.writeFile(full_path, data, function(err) {
      if(!err) {
        console.log("The file was saved!");
        res.writeHead(200, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
        let json = {};
        json['path'] = pathname;
        json['data'] = 'done';
        res.write(JSON.stringify(json) + "\n");
        res.end();        
      } else {
        // Write File Error
        res.writeHead(500, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
        let json = {};
        json['path'] = pathname;
        json['data'] = err;
        res.write(JSON.stringify(json) + "\n");
        res.end();     
      }
    }); 
  } else {
    console.log("Outside storage path");
    //Outside storage path
    res.writeHead(500, {STR_CONTENT_TYPE: STR_CONTENT_TYPE_DATA});
    let json = {};
    json['path'] = pathname;
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
  let path, method;
  
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

        path=json[STR_PATH];
        data=json[STR_DATA];
        // console.log("path[",path,"] data[",data,"]");

        if (null!=path&&null!=data){
          savefile(path,data,res);
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
    operation=query[STR_OPERATION];
    path=query[STR_PATH];
    console.log("path[",path,"]");
    if (null!=operation) {
      if ( STR_GET_FILELIST == operation ) {
        getFileList(path,res);
      }
      else if ( STR_DELETE_FILE == operation ) {
        deleteFile(path,res);
      }
      else if ( STR_GET_FILE == operation ) {
        readFile(path,res);
      }
    }
  }
  else {
    res.writeHead(404);
    res.end();
  }
}).listen(port);
console.log("Server Running on [",port,"]");