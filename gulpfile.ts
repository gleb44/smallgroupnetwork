import * as gulp from 'gulp';
import * as util from 'gulp-util';
import * as runSequence from 'run-sequence';

import Config from './tools/config';
import { loadTasks, loadCompositeTasks } from './tools/utils';

loadTasks(Config.PROJECT_TASKS_DIR);
loadCompositeTasks(Config.PROJECT_COMPOSITE_TASKS);

// --------------
// Clean dev that will only run once
let firstRun = true;
gulp.task('clean.once', (done: any) => {
    if (firstRun) {
        firstRun = false;
        runSequence('check.tools', 'clean.app', done);
    } else {
        util.log('Skipping clean on rebuild');
        done();
    }
});
