import * as gulp from 'gulp';
import * as util from 'gulp-util';
import * as runSequence from 'run-sequence';

import Config from './tools/config';
import { loadTasks } from './tools/utils';


loadTasks(Config.PROJECT_TASKS_DIR);


// --------------
// Build dev.
gulp.task('build.dev', (done: any) =>
  runSequence('clean.app',
              'build.assets.dev',
              'build.fonts',
              'build.html_css',
              'build.js.dev',
              'build.bundles.dev',
              'build.bundles.app.dev',
              'build.index.dev',
              done));

// --------------
// Build dev watch handler.
gulp.task('build.dev.watch.handler', (done:any) =>
    runSequence('build.assets.dev',
        'build.fonts',
        'build.html_css',
        'build.js.dev',
        'build.bundles.dev',
        'build.bundles.app.dev',
        'build.index.dev',
        done));

// --------------
// Build dev watch.
gulp.task('build.dev.watch', (done: any) =>
  runSequence('build.dev',
              'watch.dev',
              done));

// --------------
// Build prod.
gulp.task('build.prod', (done: any) =>
  runSequence('clean.app',
              'tslint',
              'build.assets.prod',
              'build.fonts',
              'build.html_css',
              'copy.prod',
              'build.js.prod',
              'build.bundles',
              'build.bundles.app',
              'minify.bundles',
              'build.index.prod',
              done));

// --------------
// Build prod.
gulp.task('build.prod.exp', (done: any) =>
  runSequence('check.tools',
              'clean.app',
              'tslint',
              'build.assets.prod',
              'build.html_css',
              'copy.prod',
              'compile.ahead.prod',
              'build.js.prod.exp',
              'build.bundles',
              'build.bundles.app.exp',
              'minify.bundles',
              'build.index.prod',
              done));


// --------------
// Build dev.
gulp.task('clean.application', (done: any) =>
    runSequence('clean.app',
                done));
