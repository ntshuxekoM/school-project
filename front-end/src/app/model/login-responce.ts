export class LoginResponce {
    token: string;
	type: string;
	id: number;
	username: string;
	email: string;
	roles: string;
	fullName: string;

	set setFullName(value: any) {
        this.fullName = value;
    }
}
