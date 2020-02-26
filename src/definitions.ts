declare module "@capacitor/core" {
  interface PluginRegistry {
    ContactManager: ContactManagerPlugin;
  }
}

export interface ContactManagerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getContacts(options: { query?: string }): Promise<{ data: Contact[] }>;
}


export interface Contact {
  name: string;
  id: string;
  emails: string[];
  phoneNumbers: string[];
}