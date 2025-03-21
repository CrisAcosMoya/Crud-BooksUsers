import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { config } from './app/app.config.server';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';


const serverConfig = {
  ...config,
  providers: [
    ...(config.providers || []),
    provideHttpClient(withInterceptorsFromDi())
  ]
};

const bootstrap = () => bootstrapApplication(AppComponent, serverConfig);


export default bootstrap;
