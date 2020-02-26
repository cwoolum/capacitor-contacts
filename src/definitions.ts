declare module "@capacitor/core" {
  interface PluginRegistry {
    ContactManager: ContactManagerPlugin;
  }
}

export interface ContactManagerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;

  /**
   * A method use for fetching contacts. These can be optionally filtered.
   * @param options Optional parameter for filtering clients.
   */
  getContacts(options: ContactsFilterOptions): Promise<{ data: Contact[] }>;
}

export interface ContactsFilterOptions {
  query?: String;
}

export interface Contact {
  name: string;
  id: string;
  emails: string[];
  phoneNumbers: string[];
}