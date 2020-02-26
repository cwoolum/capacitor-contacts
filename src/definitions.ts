declare module "@capacitor/core" {
  interface PluginRegistry {
    ContactManager: ContactManagerPlugin;
  }
}

export interface ContactManagerPlugin {
  /**
   * A method use for fetching contacts. These can be optionally filtered.
   * @param options Optional parameter for filtering clients.
   */
  getContacts(options: ContactsFilterOptions): Promise<{ data: Contact[] }>;
}

export interface ContactsFilterOptions {
  /**
   * The name of the contact you wish to search for.
   */
  query?: String;
}

export interface Contact {
  name: string;
  id: string;
  emails: string[];
  phoneNumbers: string[];
}