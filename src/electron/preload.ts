const { contextBridge } = require('electron/renderer');
import { SpoolManager } from '../audit/spool_manager.js';

contextBridge.exposeInMainWorld('spoolAPI', {
  getTickets: () => SpoolManager.getTickets()
});