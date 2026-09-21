export const environment = {
  production: true,
  authEnabled: true,
  apiUrl: 'PLACEHOLDER_API_GATEWAY_URL/api',
  msal: {
    clientId: 'PLACEHOLDER_FRONTEND_CLIENT_ID',
    authority: 'https://PLACEHOLDER_SUBDOMAIN.ciamlogin.com/',
    knownAuthorities: ['PLACEHOLDER_SUBDOMAIN.ciamlogin.com'],
    redirectUri: 'PLACEHOLDER_FRONTEND_URL/',
    apiScope: 'api://PLACEHOLDER_API_CLIENT_ID/access_as_user'
  }
};
