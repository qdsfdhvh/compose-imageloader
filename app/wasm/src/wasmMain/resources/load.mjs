import { instantiate } from './compose-imageloader-demo.uninstantiated.mjs';

await wasmSetup;
instantiate({ skia: Module['asm'] });