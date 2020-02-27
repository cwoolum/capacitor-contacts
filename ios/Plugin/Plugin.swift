import Foundation
import Capacitor
import Contacts

typealias JSObject = [String:Any]

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(ContactManager)
public class ContactManager: CAPPlugin {
    
    @objc func getContacts(_ call: CAPPluginCall) {
        let query = call.getString("query") ?? ""
        
        let contacts = filterContacts(query: query)

        var returnArray: [[String: Any]] = []
        
        contacts.forEach { (contact) in
            returnArray.append(contact.toDictionary())
        }
            
        var response = JSObject()
        response["data"] = returnArray
        
        call.resolve(response)
       
    }

    func filterContacts(query: String) -> [Contact] {
        let contactStore = CNContactStore()
        
        let keysToFetch = [
            CNContactFormatter.descriptorForRequiredKeys(for: .fullName),
            CNContactEmailAddressesKey,
            CNContactPhoneNumbersKey] as [Any]

        // Get all the containers
        var allContainers: [CNContainer] = []
        do {
            allContainers = try contactStore.containers(matching: nil)
        } catch {
            print("Error fetching containers")
        }

        var results: [CNContact] = []
        
        let predicate: NSPredicate = CNContact.predicateForContacts(matchingName: query)

        // Iterate all containers and append their contacts to our results array
        for container in allContainers {
            let defaultPredicate = CNContact.predicateForContactsInContainer(withIdentifier: container.identifier)

            do {
                if(query != ""){
                    let containerResults = try contactStore.unifiedContacts(matching: predicate, keysToFetch: keysToFetch as! [CNKeyDescriptor])
                    results.append(contentsOf: containerResults)
                } else {
                    
                    let containerResults = try contactStore.unifiedContacts(matching: defaultPredicate, keysToFetch: keysToFetch as! [CNKeyDescriptor])
                    results.append(contentsOf: containerResults)
                }
                
            } catch {
                print("Error fetching results for container")
            }
        }
        
        let mappedContacts = results.map { (contact) -> Contact in
            return Contact(
                name: contact.givenName + " " + contact.familyName,
                id: contact.identifier,
                emails: contact.emailAddresses.map({ (address) -> String in
                    address.label ?? ""
                }),
                phoneNumbers: contact.phoneNumbers
                    .map({ (phoneNumber) -> String in
                    phoneNumber.label ?? ""
                })
            )
        }

        return mappedContacts
    }
}
