import {join} from 'path';
import Config from '../../config';
import {clean} from '../../utils';

/**
 * Executes the build process, cleaning all files within the `/dist/dev` directory.
 */
export = clean([
    join(Config.PROJECT_ROOT, Config.DEV_DEST, 'app'),
    join(Config.PROJECT_ROOT, Config.DEV_DEST, 'assets'),
    join(Config.PROJECT_ROOT, Config.DEV_DEST, 'css'),
    join(Config.PROJECT_ROOT, Config.DEV_DEST, 'fonts'),
    join(Config.PROJECT_ROOT, Config.DEV_DEST, 'js'),

    join(Config.PROJECT_ROOT, Config.INDEX_DEST, 'index.ftl'),
    join(Config.PROJECT_ROOT, Config.DEV_DEST, 'tsconfig.json'),

    join(Config.PROJECT_ROOT, Config.TMP_DIR)
]);
