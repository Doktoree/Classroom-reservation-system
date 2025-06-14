const {app,BrowserWindow} = require("electron")
const url = require("url")
const path = require("path")


function createWindow(){

    let win = new BrowserWindow({

        title: "Moja prva electron aplikacija",
        width: 1000,
        height: 600

    })

    win.webContents.openDevTools()

    const loadUrl = url.format({

        pathname: path.join(__dirname, "./react-app/dist/index.html"),
        protocol: "file:"

    })

    //win.loadURL(loadUrl)
    win.loadURL("http://localhost:5173/")
}

app.whenReady().then(createWindow)