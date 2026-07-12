import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { resolve } from "path";

export default defineConfig({
    plugins: [react()],

    build: {
        /*
         * Il build React viene scritto direttamente
         * tra i file statici di Spring Boot.
         */
        outDir: resolve(
            __dirname,
            "../src/main/resources/static/react-commenti"
        ),

        /*
         * La cartella si trova fuori da frontend,
         * quindi diciamo esplicitamente a Vite
         * che può svuotarla prima di ogni build.
         */
        emptyOutDir: true,

        /*
         * Produciamo un solo file CSS.
         */
        cssCodeSplit: false,

        rollupOptions: {
            output: {
                entryFileNames: "commenti.js",

                chunkFileNames: "commenti-[name].js",

                assetFileNames: (assetInfo) => {
                    if (
                        assetInfo.name &&
                        assetInfo.name.endsWith(".css")
                    ) {
                        return "commenti.css";
                    }

                    return "assets/[name][extname]";
                }
            }
        }
    }
});