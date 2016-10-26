import * as gulp from 'gulp';
import * as merge from 'merge-stream';

import Config from '../../config';

/**
 * Executes the build process, bundling the shim files.
 */
export = () => merge(bundleShims());

/**
 * Returns the shim files to be injected.
 */
function getShims() {
    let libs = Config.DEPENDENCIES
        .filter(d => /\.js$/.test(d.src));

    return libs.filter(l => l.inject === 'shims')
        .concat(libs.filter(l => l.inject === 'libs'))
        .concat(libs.filter(l => l.inject === true))
        .map(l => l.src);
}

/**
 * Bundles the shim files.
 */
function bundleShims() {
    return gulp.src(getShims(), {base: './node_modules'})
        .pipe(gulp.dest(Config.JS_DEST));
}
