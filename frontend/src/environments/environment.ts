export const environment = {
  production: false,
  authEnabled: false,
  apiUrl: '/api',
  msal: {
    clientId: 'PLACEHOLDER_FRONTEND_CLIENT_ID',
    authority: 'https://login.microsoftonline.com/PLACEHOLDER_TENANT_ID',
    knownAuthorities: [] as string[],
    redirectUri: 'http://localhost:4200/',
    apiScope: 'api://PLACEHOLDER_API_CLIENT_ID/access_as_user'
  }
};
