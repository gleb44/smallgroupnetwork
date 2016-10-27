export interface InjectableDependency {
  src: string;
  inject: string | boolean;
  vendor?: boolean;
  buildType?: string[] | string;
}

export interface BuildType {
  DEVELOPMENT: string;
  PRODUCTION: string;
  [key: string]: string;
}
