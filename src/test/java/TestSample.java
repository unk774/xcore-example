import org.example.auth.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSample {

    /**
     * Проверяем все гранты, возвращаем наименьший
     * @param grantValue
     * @return
     */
    public static GrantType getGrantType(int grantValue) {
        if ((grantValue&GrantType.DENIED_VALUE) != 0) {
            return GrantType.DENIED;
        }
        if ((grantValue&GrantType.READ_VALUE) != 0) {
            return GrantType.READ;
        }
        if ((grantValue&GrantType.WRITE_VALUE) != 0) {
            return GrantType.WRITE;
        }
        return GrantType.UNKNOWN;
    }

    /**
     * Проверка гранта на объект
     * @param user
     * @param resourceObject
     * @return
     */
    public GrantType getObjectPermission(User user, ResourceObject resourceObject) {
        int result = 0;
        for (Role role: user.getRoles()) {
            result |= role.isObjectPermitted(resourceObject);
        }
        return getGrantType(result);
    }

    @Test
    public void testCreateUser() {
        //Создание пользователя и ресурса
        User user = AuthFactory.eINSTANCE.createUser();
        ResourceObject resourceObject = AuthFactory.eINSTANCE.createResourceObject();

        //Создаем первый грант запрещяющий доступ к объекту и добавляем к первой роли
        Grant grant1 = AuthFactory.eINSTANCE.createGrant();
        grant1.setGrantType(GrantType.DENIED);
        grant1.setResourceObject(resourceObject);
        Role role1 = AuthFactory.eINSTANCE.createRole();
        role1.getGrants().add(grant1);
        user.getRoles().add(role1);

        //Создаем второй грант разрешающий запись в объект и добавляем к второй роли
        Grant grant2 = AuthFactory.eINSTANCE.createGrant();
        grant2.setGrantType(GrantType.WRITE);
        grant2.setResourceObject(resourceObject);
        Role role2 = AuthFactory.eINSTANCE.createRole();
        role2.getGrants().add(grant2);
        user.getRoles().add(role2);

        //Проверям что из двух ролей получили наименьший доступ к ресурсу
        assertEquals(getObjectPermission(user, resourceObject), GrantType.DENIED);
    }
}
