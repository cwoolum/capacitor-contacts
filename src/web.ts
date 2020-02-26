import { WebPlugin } from '@capacitor/core';
import { ContactManagerPlugin, Contact } from './definitions';

export class ContactManagerWeb extends WebPlugin implements ContactManagerPlugin {
  constructor() {
    super({
      name: 'ContactManager',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async getContacts(): Promise<{ data: Contact[] }> {
    return new Promise(() => { });
  }
}

const ContactManager = new ContactManagerWeb();

export { ContactManager };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(ContactManager);
