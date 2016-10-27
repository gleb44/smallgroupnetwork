import * as gulpLoadPlugins from 'gulp-load-plugins';
import { join } from 'path';
import * as runSequence from 'run-sequence';

import Config from '../../config';
import { changeFileManager, changed, listen } from './code_change_tools';

const plugins = <any>gulpLoadPlugins();

/**
 * This utility method is used to notify that a file change has happened and subsequently calls the `changed` method,
 * which itself initiates a BrowserSync reload.
 * @param {any} e - The file that has changed.
 */
export function notifyLiveReload(e:any) {
  let fileName = e.path;
  changed(fileName);
}

/**
 * Watches the task with the given taskname.
 * @param {string} taskname - The name of the task.
 */
export function watch(taskname: string) {
  return function () {
    listen();

    let paths:string[]=[
      join(Config.APP_SRC,'**')
    ].concat(Config.TEMP_FILES.map((p) => { return '!'+p; }));

    plugins.watch(paths, (e: any) => {
      changeFileManager.addFile(e.path);

      runSequence(taskname, () => {
        changeFileManager.clear();
        notifyLiveReload(e);
      });
    });
  };
}
