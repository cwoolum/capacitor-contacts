declare module "@capacitor/core" {
  interface PluginRegistry {
    ContactManager: ContactManagerPlugin;
  }
}

export interface ContactManagerPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}
