import * as gulpLoadPlugins from 'gulp-load-plugins';
import { join } from 'path';
import * as runSequence from 'run-sequence';

import Config from '../../config';
import { changeFileManager, changed, listen } from './code_change_tools';

const plugins = <any>gulpLoadPlugins();

/**
 * Serves the Single Page Application. More specifically, calls the `listen` method, which itself launches BrowserSync.
 */
export function serveSPA() {
  listen();
}

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
export function watch(taskname: string, root: string = Config.APP_SRC) {
  return function () {
    serveSPA();

    let paths: string[] = [
      join(root, '**')
    ].concat(Config.TEMP_FILES.map((p) => { return '!' + p; }));

    plugins.watch(paths, (e: any) => {
      changeFileManager.addFile(e.path);

      // Resolves issue in IntelliJ and other IDEs/text editors which
      // save multiple files at once.
      // https://github.com/mgechev/angular-seed/issues/1615 for more details.
      setTimeout(() => {

        runSequence(taskname, () => {
          changeFileManager.clear();
          notifyLiveReload(e);
        });

      }, 100);
    });
  };
}
