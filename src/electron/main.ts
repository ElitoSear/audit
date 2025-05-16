import { app, BrowserWindow } from 'electron';
import { join } from 'path';

const createWindow = () => {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: join(app.getAppPath(), 'dist-electron/electron/preload.js'),
      contextIsolation: true,
      nodeIntegration: true,
    }
  })

  win.loadFile(join(app.getAppPath(), 'index.html'));
}

app.whenReady().then(() => {
  createWindow()

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow()
    }
  })
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})