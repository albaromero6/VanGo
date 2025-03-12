<x-guest-layout>
    <!-- Session Status -->
    <x-auth-session-status class="mb-4" :status="session('status')" />

    <form method="POST" action="{{ route('login') }}">
        @csrf

        <!-- Email -->
        <div>
        <x-input-label class="font-poppins text-xl text-tittle" for="email" :value="__('Email')" />
        <x-text-input id="email" class="block mt-1 w-full px-4 py-2 border border-ocean rounded-lg focus:outline-none focus:ring-2 focus:ring-ocean" type="email" name="email" :value="old('email')" required autofocus autocomplete="username" />
        <x-input-error :messages="$errors->get('email')" class="mt-2" />

        </div>

        <!-- Password -->
        <div class="mt-4">
            <x-input-label class="font-poppins text-xl text-tittle" for="password" :value="__('Password')" />

            <x-text-input id="password" class="block mt-1 w-full px-4 py-2 border border-ocean rounded-lg focus:outline-none focus:ring-2 focus:ring-ocean"
                            type="password"
                            name="password"
                            required autocomplete="current-password" />

            <x-input-error :messages="$errors->get('password')" class="mt-2" />
        </div>

        <!-- Remember Me -->
        <div class="mt-4">
            <label for="remember_me" class="inline-flex items-center">
                <input id="remember_me" type="checkbox" class="rounded text-ocean focus:ring-ocean" name="remember">
                <span class="ms-2 text-sm text-gray-600 dark:text-gray-400">{{ __('Remember me') }}</span>
            </label>
        </div>

        <div class="flex items-center justify-end mt-4 font-poppins">
            @if (Route::has('password.request'))
                <a class="underline text-sm text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-gray-100 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 dark:focus:ring-offset-gray-800" href="{{ route('password.request') }}">
                    {{ __('Forgot your password?') }}
                </a>
            @endif

            <x-primary-button class="ms-3 font-poppins bg-ocean text-white hover:bg-hovers focus:ring-2 focus:ring-offset-2 focus:ring-ocean">
                {{ __('Log in') }}
            </x-primary-button>
        </div>
    </form>
</x-guest-layout>