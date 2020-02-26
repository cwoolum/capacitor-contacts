import { WebPlugin } from '@capacitor/core';
import { ContactManagerPlugin, Contact } from './definitions';

export class ContactManagerWeb extends WebPlugin implements ContactManagerPlugin {
  constructor() {
    super({
      name: 'ContactManager',
      platforms: ['web']
    });
  }

  async getContacts(options: { query?: string }): Promise<{ data: Contact[] }> {
    console.log(options.query);
    return new Promise(() => { });
  }
}

const ContactManager = new ContactManagerWeb();

export { ContactManager };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(ContactManager);
