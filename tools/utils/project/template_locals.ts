import Config from '../../config';

/**
 * Builds an object consisting of the base configuration provided by confg/seed.config.ts, the additional
 * project specific overrides as defined in config/project.config.ts and including the base environment config as defined in env/base.ts
 * and the environment specific overrides (for instance if env=dev then as defined in env/dev.ts).
 */
export class TemplateLocalsBuilder {
  private stringifySystemConfigDev = false;

  withStringifiedSystemConfigDev() {
    this.stringifySystemConfigDev = true;
    return this;
  }

  build() {
    let locals = Object.assign({}, Config);
    if (this.stringifySystemConfigDev) {
      Object.assign(locals, {SYSTEM_CONFIG_DEV: JSON.stringify(Config.SYSTEM_BUILDER_CONFIG_DEV)});
    }
    return locals;
  }
}

