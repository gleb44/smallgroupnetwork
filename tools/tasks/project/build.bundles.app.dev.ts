import { join } from 'path';

import * as gulp from 'gulp';
import * as Builder from 'systemjs-builder';

import Config from '../../config';

const BUNDLER_OPTIONS = {
    format: 'cjs',
    minify: false,
    mangle: false
};

/**
 * Executes the build process, bundling the JavaScript files using the SystemJS builder.
 */
export = (done: any) => {

    // Copy from paths

    let modules: string[] = [];
    for (var prop in Config.SYSTEM_BUILDER_CONFIG_DEV.paths) {
        console.log('From paths -----> ' + Config.SYSTEM_BUILDER_CONFIG_DEV.paths[prop]);
        modules.push(<string>Config.SYSTEM_BUILDER_CONFIG_DEV.paths[prop]);
    }
    gulp.src(modules.filter(d => d.startsWith('node_modules') && d.indexOf('*') == -1), {base: './node_modules'})
        .pipe(gulp.dest(Config.JS_DEST));

    // Copy from packages

    let builder = new Builder(Config.SYSTEM_BUILDER_CONFIG_DEV);
    builder
        .buildStatic(`${Config.DEV_DEST}/${Config.BOOTSTRAP_MODULE}`,
            null,
            BUNDLER_OPTIONS)
        .then((output) => {
            let paths:string[] = output.modules;
            paths = paths
                .filter(d => !d.startsWith(Config.DEV_DEST))
                .map(d => join(Config.PROJECT_ROOT, 'node_modules', d));

            for (var item of paths) {
                console.log('From packages -----> ' + item);
            }

            gulp.src(paths, {base: './node_modules'})
                .pipe(gulp.dest(Config.JS_DEST));

            done();
        })
        .catch((err: any) => done(err));
};
